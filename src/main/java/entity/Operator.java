package entity;

import lombok.Data;

import javax.persistence.Column;

/*
 * @author by cmc
 * @date 2019/3/20 15:10
 */
@Data
public final class Operator extends BaseEntity {

    @Column(name = "name", length =  40)
    private String name;

    @Column(name = "level")
    private int level;


    private boolean disacard;
}
