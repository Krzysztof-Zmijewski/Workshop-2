package pl.coderslab.entity;

public class MainDao {
    public static void main(String[] args) {
//        User user = new User();
//        user.setUserName("testowy użytkownik 2");
//        user.setEmail("random2@gmail.com");
//        user.setPassword("mammamia0201");
        UserDao userDao = new UserDao();
//        userDao.create(user);
        User read = userDao.read(4);
        System.out.println(read.getUserName());
    }
}
