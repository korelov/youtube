package org.javaacademy;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.javaacademy.entity.Comment;
import org.javaacademy.entity.User;
import org.javaacademy.entity.Video;

import java.util.List;
import java.util.Properties;

public class Runner {

    public static void main(String[] args) {
        @Cleanup SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Video.class)
                .addAnnotatedClass(Comment.class)
                .addProperties(createProperties()).buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        // 5.2 создать двух пользователей - john, rick
        User john = new User("john");
        User rick = new User("rick");
        session.persist(john);
        session.persist(rick);
        //5.3 сделать два видео для john: Мое первое интервью, Мое второе интервью (любое описание для двух видео).
        Video johnFirstInterview =
                new Video("Мое первое интервью", "собеседование, которое я завалил", john);
        Video johnSecondInterview =
                new Video("Мое второе интервью", "собеседование, которое я прошел", john);
        session.persist(johnFirstInterview);
        session.persist(johnSecondInterview);
        //5.4 сделать на первое видео комментарий от rick - "классное интервью".
        Comment rickComment = new Comment(johnFirstInterview, rick, "классное интервью");
        session.persist(rickComment);
        session.getTransaction().commit();
        //5.5 очистить кэш hibernate (session.clear())
        session.clear();
        //5.6 Вытащить из бд пользователя john, у него вытащить видео "Мое первое интервью".
        // Вытащить из него комментарий и распечатать.
        String sql = """
                select comments.description from users
                left join videos on users.id = videos.user_id
                left join comments on videos.id = comments.video_id
                where users.nick_name = 'john' and videos.title = 'Мое первое интервью'
                """;
        List resultList = session.createNativeQuery(sql).getResultList();
        System.out.println(resultList);

        Query<User> query = session.createQuery("from User WHERE nickName = 'john'", User.class);
        User johnFromDataBase = query.getSingleResult();
        Video firstInterview = johnFromDataBase.getVideoList().stream()
                .filter(video -> video.getTitle().equals("Мое первое интервью"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет такого видео"));
        List<Comment> comments = firstInterview.getComments();
        if (!comments.isEmpty()) {
            System.out.println(comments.get(0).getDescription());
        }
    }

    private static Properties createProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/youtube");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "3012413");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        return properties;
    }
}
