package com.example.snapish.repository;

import android.graphics.Bitmap;
import com.example.snapish.ITaskListener;
import com.example.snapish.IUpdateble;
import com.example.snapish.model.Snap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    //owned by the repository class (singleton)
    private static Repository repository = new Repository();

    //connection to data base
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Snap> snaps = new ArrayList<>();
    private final String COLLECTION_PATH = "snaps";
    private IUpdateble activity;

    public static Repository repository() {
        return repository; }

    public void setup(IUpdateble a, List<Snap> list) {
        activity = a;
        snaps = list;
        startListener();
    }

    public void startListener(){
        db.collection(COLLECTION_PATH).addSnapshotListener((values, error) ->{
            //clearing the data to avoid duplications
            snaps.clear();
            for(DocumentSnapshot snap: values.getDocuments()){
                snaps.add(new Snap(snap.getId()));
            }
            // have a reference to mainactivity, and call a update()
            activity.update(null);
        });
    }

    public void uploadBitmap(Bitmap bitmap, String imageText){
        System.out.println("test");
        // Creating a reference to our firebase named doc
        DocumentReference doc = db.collection(COLLECTION_PATH).document();
        // Mapping doc into keys & values
        Map<String, String> map = new HashMap<>();
        doc.set(map);
        String id = doc.getId();

        StorageReference ref = storage.getReference(id);
        // Converting data to bytes by using BAOAS
        ByteArrayOutputStream baoas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baoas);
        //lambda expression
        ref.putBytes(baoas.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("upload complete" + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("upload failed" + exception);
        });
    }

    public void downloadBitmap(String id, ITaskListener taskListener){
        StorageReference ref = storage.getReference(id);
        int max = 1920 * 1080; // Screen size
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            taskListener.receive(bytes);
            System.out.println("Download OK");
        }).addOnFailureListener(ex -> {
            System.out.println("error in download " + ex);
        });
    }

    //Deleting the image in firebase
    public void deleteImage(Snap image){
        // Finding the image path in firebase
        DocumentReference documentReference = db.collection(COLLECTION_PATH).document(image.getId());
        // Deleting the image
        documentReference.delete();
        //Finding the image path from google cloud storage
        StorageReference storageReference = storage.getReference(image.getId());
        //Deleting the image
        storageReference.delete();
    }
}
