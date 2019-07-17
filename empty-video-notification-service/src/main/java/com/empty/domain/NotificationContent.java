package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationContent {
    private NotificationField field;
    private String text;

    //four Id strings are not necessary.
    private String commentId;
    private String userId;
    private String favListId;
    private String videoId;
}
