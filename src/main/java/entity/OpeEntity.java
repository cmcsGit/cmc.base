package entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author cmc
 */
@Data
@MappedSuperclass
public class OpeEntity extends BaseEntity{

    public OpeEntity(){

    }

    public OpeEntity(Integer createId){
        this.creatorId = createId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate",nullable = false,columnDefinition = "DATETIME")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modifyDate",columnDefinition = "DATETIME")
    private Date modifyDate;

    @Column(name="creatorId")
    private Integer creatorId;

    @Column(name="modifierId")
    private Integer modifierId;

    @Column(name = "creatorName",length = 50)
    private String creatorName;

    @Column(name = "modifierName",length = 50)
    private String modifierName;

    public void modifyRecord(int modifierId, String modifierName){
        this.modifierId = modifierId;
        this.modifierName = modifierName;
        this.modifyDate = new Date();
    }

    public void createRecord(int creatorId, String creatorName) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.createDate = new Date();
    }
}
