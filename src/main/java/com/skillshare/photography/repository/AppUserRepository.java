package com.skillshare.photography.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.skillshare.photography.model.AppUser;

@Repository
public class AppUserRepository {

    private final MongoCollection<Document> collection;

    public AppUserRepository(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("photography");
        this.collection = database.getCollection("appUsers");
    }

    public AppUser findByEmail(String email) {
        Document doc = collection.find(eq("email", email)).first();
        if (doc == null) return null;
        return documentToAppUser(doc);
    }

    public AppUser findById(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        if (doc == null) return null;
        return documentToAppUser(doc);
    }

    public void save(AppUser user) {
        Document doc = new Document()
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("name", user.getFullName());
        if (user.getId() != null) {
            doc.append("_id", new ObjectId(user.getId()));
            collection.replaceOne(eq("_id", new ObjectId(user.getId())), doc);
        } else {
            collection.insertOne(doc);
            user.setId(doc.getObjectId("_id").toHexString());
        }
    }

    private AppUser documentToAppUser(Document doc) {
        AppUser user = new AppUser();
        user.setId(doc.getObjectId("_id").toHexString());
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setFullName(doc.getString("name"));
        return user;
    }
}
