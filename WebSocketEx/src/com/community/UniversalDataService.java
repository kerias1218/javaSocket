package com.community;

import javax.servlet.http.HttpSession;

import coreframe.data.DataSet;
import coreframe.data.InteractionBean;
import coreframe.data.ResourceException;
import corelogic.spi.management.BLContextFactory;

/**
 * <b><code>UniversalDataService</code></b>
 * <p>
 * coreframe에서 데이터 입출력을 위해 BLD를 실행하는 InteractionBean을 Wrapping한 클래스로
 * BLD수행시 공통 처리되어야 하는 작업들을 추가한 클래스이다. 기본 InteractionBean을 쓰기 보다는
 * UniversalDataService 를 이용하여 처리한다.
 * </p>
 * 
 * @author neoxeni <a href="mailto:neoxeni@cyber-i.com">neoxeni@cyber-i.com</a>
 * @version 1.0
 * @since 2017-04-13
 * 
 */
public class UniversalDataService {
    // private final static String LOG_ID = "<UniversalDataService> ";
    // private UserSession user;

    private BLContextFactory bl_factory = BLContextFactory.getInstance();
    private InteractionBean interact = null;

    /**
     * 
     * @param session
     */
    public UniversalDataService(HttpSession session) {
        // user = (UserSession) session.getAttribute(UserSession.SESSION_KEY);
        interact = new InteractionBean();
    }

    /**
     * 
     */
    public UniversalDataService() {
        interact = new InteractionBean();
    }

    /**
     * execute
     * 
     * @param bldName
     * @param input
     * @return
     * @throws ResourceException
     */
    public DataSet execute(String bldName, DataSet input) throws ResourceException {

        DataSet output = executeBL(bldName, input);

        return output;
    }

    /**
     * BLD를 수행한다.
     * 
     * @param bldName
     * @param input
     * @return
     * @throws ResourceException
     */
    private DataSet executeBL(String bldName, DataSet input) throws ResourceException {
        return interact.execute(bldName, input);
    }

    /**
     * InteractionBean 객체를 얻는다.
     * 
     * @return
     */
    private InteractionBean getInteractionBean() {
        return interact;
    }

    public void beginTransaction() throws ResourceException {
        interact.beginTransaction();
    }

    public void commitTransaction() throws ResourceException {
        interact.commitTransaction();
    }

    public void rollbackTransaction() throws ResourceException {
        interact.rollbackTransaction();
    }

    public void endTransaction() throws ResourceException {
        interact.endTransaction();
    }
}
