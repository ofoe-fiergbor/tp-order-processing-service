package com.group19.orderprocessingservice.services.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.group19.orderprocessingservice.domain.dto.MessageDto;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public class MessagingService {

    public static final String COL_NAME="messages";

    public String saveMessage(MessageDto messageDto) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(messageDto.getId()).set(messageDto);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

}
