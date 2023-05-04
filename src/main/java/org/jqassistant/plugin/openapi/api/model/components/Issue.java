package org.jqassistant.plugin.openapi.api.model.components;

import com.buschmais.xo.neo4j.api.annotation.Label;

import java.util.List;
@Label("Issue")
public interface Issue {

    String getTitle();
    void setTitle(String title);
    String getDescription();
    void setDescription(String description);
    IssueType getType();
    void setType(IssueType type);
    List<Comment> getComments();
    void addComment(Comment comment);

}
