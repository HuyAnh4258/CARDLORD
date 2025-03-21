package model;

import java.sql.Timestamp;

public class FlashcardSet {
    
    private int flashcard_set_id; // Primary key, auto-increment
    private Timestamp time_created; // Auto-generated timestamp
    private int user_id; // Foreign key, not null
    private String name_of_flashcardset;
    private String status;  // "New", "Learning", "Known", "Forgotten"
    private float score; 
    private String tags;
    private String visibility; // "Everyone" | "Only me"
    private int view; // Default 0

    // Constructor
    public FlashcardSet(int flashcard_set_id, Timestamp time_created, int user_id, 
                        String name_of_flashcardset, String status, float score, 
                        String tags, String visibility, int view) {
        this.flashcard_set_id = flashcard_set_id;
        this.time_created = time_created;
        this.user_id = user_id;
        this.name_of_flashcardset = name_of_flashcardset;
        this.status = status;
        this.score = score;
        this.tags = tags;
        this.visibility = visibility;
        this.view = view;
    }

    // Getters and Setters
    public int getFlashcard_set_id() {
        return flashcard_set_id;
    }

    public void setFlashcard_set_id(int flashcard_set_id) {
        this.flashcard_set_id = flashcard_set_id;
    }

    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName_of_flashcardset() {
        return name_of_flashcardset;
    }

    public void setName_of_flashcardset(String name_of_flashcardset) {
        this.name_of_flashcardset = name_of_flashcardset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
