package com.empty.service;

import com.empty.domain.FavList;
import com.empty.domain.OperationEnum;
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

    public Mono<ServerResponse> update(ServerRequest req1) {
        String favListId = req1.pathVariable("id");
        Mono<FavList> favListMono = favListRepository.findById(favListId);
        return Mono.zip(favListMono, Mono.just(req1)).flatMap(tuple -> {
            FavList find = tuple.getT1();
            ServerRequest req2 = tuple.getT2();
            String authId = String.valueOf(req2.attributes().get("authId"));
            String operation = req2.queryParam("operation").get();
            if (find != null && find.getUserId().equals(authId)) {
                switch (OperationEnum.valueOf(operation)) {
                    case FAV_A_VIDEO:
                        String videoId = req2.queryParam("videoId").get();
                        find.getVideoIds().add(videoId);
                }
                req2.attributes().put("favList", find);
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
            ServerRequest req2 = tuple.getT2();
            String authId = String.valueOf(req2.attributes().get("authId"));
            req2.attributes().put("favList", favList);
            if (authId.equals(favList.getUserId())) {
                favList.setDeleted(true);
                return favListRepository.save(favList).then(ok().build());
            } else {
                return status(403).build();
            }
        });
    }

    public Mono<ServerResponse> create(ServerRequest req1) {
        Mono<FavList> newFavListMono = req1.bodyToMono(FavList.class);
        String authId = String.valueOf(req1.attributes().get("authId"));
        return Mono.zip(newFavListMono, Mono.just(authId)).flatMap(tuple -> {
            FavList newItem = tuple.getT1();
            newItem.setUserId(tuple.getT2());
            return favListRepository.save(newItem).flatMap(favList -> {
                req1.attributes().put("favList", favList);
                return Mono.just(favList);
            }).then(status(201).build());
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
