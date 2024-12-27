
package repository;

import model.NhanVien;

/**
 *
 * @author ledin
 */
public class Auth {
    public static NhanVien user = null;
    
    public static void clear(){
        Auth.user = null;
    }
    
    public static Boolean isLogin(){
        return Auth.user != null;
    }
    
    public static Boolean isManager(){
        return Auth.isLogin() && Auth.user.getVaiTro() == 1;
    }
}
