package de.deingreenstream.app.data;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
    private long id;
    private Label label;
    private String text;

    public Feedback() {}

    public Feedback(long id, Label label, String text) {
        this.id = id;
        this.label = label;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(label.getId()));
        map.put("information_id", String.valueOf(id));
        map.put("feedback", text);
        return map;
    }
}
