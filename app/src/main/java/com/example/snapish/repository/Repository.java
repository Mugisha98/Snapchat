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

    //med til at implementere signleton, så der kun er en kopi af dette objekt.
    private static Repository repository = new Repository();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Snap> snaps = new ArrayList<>();
    private final String COLLECTION_PATH = "snaps";
    private IUpdateble activity;

    //constuctor til singleton
    public static Repository repository() {
        return repository; }

    public void setup(IUpdateble a, List<Snap> list) {
        activity = a;
        snaps = list;
        startListener();

    }

    public void startListener(){
        db.collection(COLLECTION_PATH).addSnapshotListener((values, error) ->{
            //Denne funktion clear alt vores data og lægger det nye ovenpå. På denne måde får vi ikke dupplicates
            snaps.clear();
            for(DocumentSnapshot snap: values.getDocuments()){
                snaps.add(new Snap(snap.getId()));
            }
            // have a reference to mainactivity, and call a update()
            activity.update(null);
        });
    }

//    // denne metode skal kaldes fra myProfil
//    public void profilInfo(TextView usernameText, TextView nameText, TextView emailText, TextView biografiText){
//        //indsæter info til din profil
//        DocumentReference reference = db.collection("profil").document();
//        Map<String, TextView> map = new HashMap<>();
//        map.put("brugernavn", usernameText);
//        reference.set(map);
//        map.put("navn", nameText);
//        reference.set(map);
//        map.put("email", emailText);
//        reference.set(map);
//        map.put("biografi", biografiText);
//        reference.set(map);
//
//        System.out.println("done med bruger info" + reference.getId());
//    }

    //_____________________________________________________________ upload/dl/delete metoder

    public void uploadBitmap(Bitmap bitmap, String imageText){
        System.out.println("test");
        // Vi laver en reference til et dokument i firebase som vi kalder doc
        DocumentReference doc = db.collection(COLLECTION_PATH).document();
        // Herefter får vi lavet et Map, hvor vi sætter doc til at blive map fordi vi skal bruge key/value
        Map<String, String> map = new HashMap<>();
        doc.set(map);
        String id = doc.getId();

        StorageReference ref = storage.getReference(id);
        //Vi ioretter Baoas for at converte data til byyes
        ByteArrayOutputStream baoas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baoas);
        //Her tjekkes der for om det bliver added eller ej med et lamda expression
        ref.putBytes(baoas.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("upload complete" + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("upload failed" + exception);
        });
    }

    public void downloadBitmap(String id, ITaskListener taskListener){ // Skal bruges til at dl billede til listen på forside onClick
        StorageReference ref = storage.getReference(id);
        int max = 1920 * 1080; // you are free to set the limit here
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            taskListener.receive(bytes);
            System.out.println("Download OK");
        }).addOnFailureListener(ex -> {
            System.out.println("error in download " + ex);
        });
    }

    //Metode til at slette når vi lukker aktiviteten
    public void deleteImage(Snap image){
        //her laves en reference til et dokuments lokation i firebase, som vi vælger at slette efter
        DocumentReference documentReference = db.collection(COLLECTION_PATH).document(image.getId());
        documentReference.delete();
        //her er en reference til et Google Cloud Storage object som vi så sletter på linjen under
        StorageReference storageReference = storage.getReference(image.getId());
        storageReference.delete();
    }
//_________________________________________________________________________________________

}
