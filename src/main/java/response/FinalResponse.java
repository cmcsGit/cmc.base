package response;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Encryption;
import util.HttpUtil;
import util.JsonUtil;
import util.ResponseUtil;
import util.impl.Md5Util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author cmc
 */
public class FinalResponse implements HttpUtil,JsonUtil,ResponseUtil{

    /** 独立验证信息*/
    private int cm;

    private Encryption encry;

    private Boolean success;

    private String message;

    private String retDate;

    private Long passedTime;
    
    private final Logger log = LoggerFactory.getLogger(FinalResponse.class);

    public FinalResponse(){
        success = true;
    }

    public FinalResponse(String message){
        this.message = message;
        success = false;
    }

    @Override
    public void writeBack(HttpServletResponse response){

        log.info("!---------开始返回数据");

        OutputStream out = null;
        try {
            log.info("!-----------设置消息头");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            out = response.getOutputStream();

            log.info("!-----------写入数据");
            out.write(toString().getBytes("UTF-8"));
            out.close();
            log.info("!-----------写入完成,返回完毕");
        }catch(Exception e){
            log.error("!-----------返回数据失败" + e.getMessage());
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e1) {
                log.error("!----------关闭数据流管道失败" + e1.getMessage());
            }
        } finally{
            log.info("!----------------------------!");
        }
    }

    @Override
    public String toString(){
        JSONObject ret = new JSONObject();
        ret.put("success",success);
        ret.put("message",message);
        ret.put("data",retDate);
        return ret.toString();
    }

    public FinalResponse setEncry(String encName){

        switch (encName){
            default: this.encry = new Md5Util();break;
        }

        return this;
    }

    public FinalResponse encrypt(){
        if(this.encry == null){
            encry = new Md5Util();
        }

        this.retDate = encry.encrypt(this.retDate);
        return this;
    }

    @Override
    public Boolean getSuccess() {
        return success;
    }

    @Override
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
        success = false;
    }

    @Override
    public String getRetDate() {
        return this.retDate;
    }

    @Override
    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    @Override
    public Long getPassedTime() {
        return passedTime;
    }

    @Override
    public void setPassedTime(Long passedTime) {
        this.passedTime = passedTime;
    }

    public int getCm() {
        return cm;
    }

    public void setCm(int cm){
        this.cm = cm;
    }

    public Encryption getEncry() {
        return encry;
    }

    public void setEncry(Encryption encry) {
        this.encry = encry;
    }

    @Override
    public String toStr(Object entity) {
        return null;
    }

    @Override
    public String toStr() {
        return null;
    }
}
