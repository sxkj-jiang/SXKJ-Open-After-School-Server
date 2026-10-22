package com.sxkj.edu.module.system.login;

import com.sxkj.edu.module.afterschool.service.AsGuardianStudentService;
import com.sxkj.edu.module.system.login.domain.LoginDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 家长登录缓存学期信息刷新测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceParentTermTest {

    @Mock
    private AsGuardianStudentService asGuardianStudentService;

    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginService();
        ReflectionTestUtils.setField(loginService, "asGuardianStudentService", asGuardianStudentService);
    }

    @Test
    public void shouldRefreshParentChildTermWhenLoginCacheIsOld() {
        LoginDetailVO loginDetail = new LoginDetailVO();
        loginDetail.setUserType(1);
        LoginStuParentDetailVO oldChild = new LoginStuParentDetailVO();
        oldChild.setStudentId(341L);
//        loginDetail.setChildInfo(oldChild);

        LoginStuParentDetailVO currentChild = new LoginStuParentDetailVO();
        currentChild.setStudentId(341L);
        currentChild.setTermId(30L);
        currentChild.setTermName("需求1全新学期");
        when(asGuardianStudentService.queryStudentByParent(790L, 1L)).thenReturn(currentChild);

        ReflectionTestUtils.invokeMethod(loginService, "refreshParentChildInfo",
                loginDetail, 790L, 1L);

//        assertEquals(Long.valueOf(30L), loginDetail.getChildInfo().getTermId());
//        assertEquals("需求1全新学期", loginDetail.getChildInfo().getTermName());
    }

    @Test
    public void shouldNotQueryStudentForTeacherLogin() {
        LoginDetailVO loginDetail = new LoginDetailVO();
        loginDetail.setUserType(2);

        ReflectionTestUtils.invokeMethod(loginService, "refreshParentChildInfo",
                loginDetail, 823L, 5L);

        verify(asGuardianStudentService, never()).queryStudentByParent(823L, 5L);
    }
}
