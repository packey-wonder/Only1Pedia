package org.vaadin.backend.domain;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * A standard JPA entity, like in any other Java application.
 */
@NamedQueries({
        @NamedQuery(name="Book.findAll",
                query="SELECT c FROM Book c"),
        @NamedQuery(name="Book.findByName",
                query="SELECT c FROM Book c WHERE LOWER(c.heading) LIKE :filter OR LOWER(c.body) LIKE :filter"),
        @NamedQuery(name="Book.findByTitle",
        		query="SELECT c FROM Book c WHERE LOWER(c.title) LIKE :filter")    
})
@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    
    private int seq;
    
    private String heading;

    private String body;

    private String link;
    
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the value of seq
     *
     * @return the value of status
     */
    public int getSeq() {
        return seq;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * Get the value of heading
     *
     * @return the value of heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Set the value of heading
     *
     * @param heading new value of heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }
    
    /**
     * Get the value of body
     *
     * @return the value of body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the value of body
     *
     * @param body new value of body
     */
    public void setBody(String body) {
        this.body = body;
    }

/**
     * Get the value of link
     *
     * @return the value of link
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the value of link
     *
     * @param link new value of link
     */
    public void setlink(String link) {
        this.link = link;
    }


    public boolean isPersisted() {
        return id > 0;
    }

}
