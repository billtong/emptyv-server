package com.empty.service;

import com.empty.domain.FavList;
import com.empty.repository.FavListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
@Slf4j
public class FavListService {
    @Autowired
    FavListRepository favListRepository;

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String favListId = serverRequest.pathVariable("id");
        return favListRepository.findById(favListId).flatMap(favList -> ok().body(Mono.just(favList), FavList.class));
    }

    public Mono<ServerResponse> getUsersFavList(ServerRequest serverRequest) {
        String userId = String.valueOf(serverRequest.queryParam("userId"));
        return favListRepository.findAllByUserIdOrderByCreatedAsc(userId).collectList()
                .flatMap(favLists -> ok().body(Mono.just(favLists), List.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String favListId = serverRequest.pathVariable("id");
        Mono<FavList> updateFavListMono = serverRequest.bodyToMono(FavList.class);
        Mono<FavList> favListMono = favListRepository.findById(favListId);
        String authId = String.valueOf(serverRequest.attributes().get("authId"));
        return Mono.zip(updateFavListMono, favListMono, Mono.just(authId)).flatMap(tuple -> {
            FavList update = tuple.getT1();
            FavList find = tuple.getT2();
            String userId = tuple.getT3();
            if (find != null && find.getUserId().equals(userId)) {
                find.setVideoIds(update.getVideoIds());
                return favListRepository.save(find).then(ok().build());
            } else {
                return status(403).body(Mono.just("didn't find the favlist or you are not the creator"), String.class);
            }
        });
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String favListId = serverRequest.pathVariable("id");
        Mono<FavList> favListMono = favListRepository.findById(favListId);
        return Mono.zip(favListMono, Mono.just(serverRequest)).flatMap(tuple -> {
            FavList favList = tuple.getT1();
            String authId = String.valueOf(tuple.getT2().attributes().get("authId"));
            if (authId.equals(favList.getUserId())) {
                favList.setDeleted(true);
                return favListRepository.save(favList).then(ok().build());
            } else {
                return status(403).build();
            }
        });
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<FavList> newFavListMono = serverRequest.bodyToMono(FavList.class);
        String authId = String.valueOf(serverRequest.attributes().get("authId"));
        return Mono.zip(newFavListMono, Mono.just(authId)).flatMap(tuple -> {
            FavList newItem = tuple.getT1();
            newItem.setUserId(tuple.getT2());
            return favListRepository.save(newItem).then(status(201).build());
        });
    }

    public Mono<ServerResponse> search(ServerRequest serverRequest) {
         /*
            need elacstic search engine and real data to actually implement this method.
            wait for it.
         */
        return ok().build();
    }
}
