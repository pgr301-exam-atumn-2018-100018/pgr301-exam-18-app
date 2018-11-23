package app.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bucketlist
{
    @Id
    @GeneratedValue
    private Long id;
    private String listItem;

    public Bucketlist() { }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getListItem() {
        return listItem;
    }
    public void setListItem(String listItem) { this.listItem = listItem; }
}