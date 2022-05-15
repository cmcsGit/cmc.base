package entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author cmc
 */
@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @Column(name = "VERSION__",nullable = false,columnDefinition = "timestamp",insertable = false,updatable = false)
    private Byte[] version__;
}
