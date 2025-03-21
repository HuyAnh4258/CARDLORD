/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author daizl
 */
public class FlashCard {

    private int flashcard_id;
    private int flashcard_set_id;
    private String term;
    private String flashcard_definition;

    public FlashCard() {
    }

    public FlashCard(int flashcard_id, int flashcard_set_id, String term, String flashcard_definition) {
        this.flashcard_id = flashcard_id;
        this.flashcard_set_id = flashcard_set_id;
        this.term = term;
        this.flashcard_definition = flashcard_definition;
    }

    public int getFlashcard_id() {
        return flashcard_id;
    }

    public void setFlashcard_id(int flashcard_id) {
        this.flashcard_id = flashcard_id;
    }

    public int getFlashcard_set_id() {
        return flashcard_set_id;
    }

    public void setFlashcard_set_id(int flashcard_set_id) {
        this.flashcard_set_id = flashcard_set_id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getFlashcard_definition() {
        return flashcard_definition;
    }

    public void setFlashcard_definition(String flashcard_definition) {
        this.flashcard_definition = flashcard_definition;
    }

}
