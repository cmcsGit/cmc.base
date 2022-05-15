package util.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author cmc
 */
public class RepConfig {

    private SimpleDateFormat sdf;

    private BigDecimal defaultDec;

    static BigDecimal zero;

    private int accuracy;

    private int scaleType;

    public RepConfig(){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        accuracy = 2;
        scaleType = BigDecimal.ROUND_HALF_UP;
        zero = new BigDecimal(0);
        defaultDec = new BigDecimal(0.0);
    }

    public SimpleDateFormat getFormatDate() {
        return sdf;
    }

    public void setFormatDate(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public void setFormatDate(String format){
        this.sdf = new SimpleDateFormat(format);
    }

    public void setDefaultDecimal(BigDecimal dec){
        this.defaultDec = dec;
    }

    public BigDecimal getDefaultDecimal(){
        return this.defaultDec;
    }

    public void setAccuracy(int accuracy){
        this.accuracy = accuracy;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getScaleType() {
        return scaleType;
    }

    /**
     *
     * @param scaleType BigDecimal.ROUND_DOWN
     *                  BigDecimal.ROUND_HALF_UP
     *                  BigDecimal.ROUND_UP
     *                  BigDecimal.ROUND_HALF_DOWN
     */
    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }
}
