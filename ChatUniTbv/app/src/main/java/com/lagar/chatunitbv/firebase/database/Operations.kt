import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Operations{

    val database by lazy { Firebase.firestore }
}