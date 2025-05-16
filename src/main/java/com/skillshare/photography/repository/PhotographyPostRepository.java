package com.skillshare.photography.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.skillshare.photography.model.PhotographyPost;

@Repository
public class PhotographyPostRepository {

    private final MongoCollection<Document> collection;

    public PhotographyPostRepository(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("photography");
        this.collection = database.getCollection("photographyPosts");
    }

    // Save PhotographyPost
    public void save(PhotographyPost post) {
        Document doc = new Document()
                .append("title", post.getTitle())
                .append("description", post.getDescription())
                .append("imageUrl", post.getImageUrl())
                .append("userId", post.getUserId());

        if (post.getId() != null) {
            doc.append("_id", new ObjectId(post.getId()));
            collection.replaceOne(new Document("_id", new ObjectId(post.getId())), doc);
        } else {
            collection.insertOne(doc);
            post.setId(doc.getObjectId("_id").toHexString());
        }
    }

    // Find by ID
    public PhotographyPost findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        if (doc == null) return null;
        return documentToPhotographyPost(doc);
    }

    // Find all posts
    public List<PhotographyPost> findAll() {
        List<PhotographyPost> posts = new ArrayList<>();
        for (Document doc : collection.find()) {
            posts.add(documentToPhotographyPost(doc));
        }
        return posts;
    }

    // Delete by ID
    public void deleteById(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    private PhotographyPost documentToPhotographyPost(Document doc) {
        PhotographyPost post = new PhotographyPost();
        post.setId(doc.getObjectId("_id").toHexString());
        post.setTitle(doc.getString("title"));
        post.setDescription(doc.getString("description"));
        post.setImageUrl(doc.getString("imageUrl"));
        post.setUserId(doc.getString("userId"));
        return post;
    }
}
