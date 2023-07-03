package ru.yandex.practicum.filmorate.review.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.film.storage.FilmDBStorage;
import ru.yandex.practicum.filmorate.review.model.Review;
import ru.yandex.practicum.filmorate.user.storage.UserDBStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("reviewDbStorage")
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDBStorage fs;
    private final UserDBStorage us;


    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("filmDBStorage") FilmDBStorage fs,
            @Qualifier("userDBStorage") UserDBStorage us) {
        this.jdbcTemplate = jdbcTemplate;
        this.fs = fs;
        this.us = us;
    }


    @Override
    public Review saveReview(Review review) {
        fs.getFilmById(review.getFilmId());
        us.getUserById(review.getUserId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviews")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(reviewToMap(review));
        review.setReviewId((Long) key);
        log.debug("Review на Film c ID {} от User c ID {} создан", review.getFilmId(), review.getUserId());
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        String sqlQuery = "SELECT ID, CONTENT, ISPOSITIVE, USERID, FILMID " +
                "FROM PUBLIC.REVIEWS where ID = ? ";
        Review review;
        try {
            review = jdbcTemplate.queryForObject(sqlQuery, ReviewDbStorage::mapToReview, id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("review " + id + " not found");
        }
        log.debug("Review с ID {} на Film c ID {} от User c ID {} получено",
                review.getReviewId(), review.getFilmId(), review.getUserId());
        return review;
    }

    @Override
    public List<Review> readAllReviews(Long filmId, Long count) {
        List<Review> reviews;
        if (filmId == null) {
            String sqlQuery = "SELECT R.ID, " +
                    "r.CONTENT, " +
                    "r.ISPOSITIVE, " +
                    "r.USERID, " +
                    "r.FILMID, " +
                    "COUNT(NULLIF(POSITIVE, false))-COUNT(NULLIF(POSITIVE,true)) useful " +
                    "FROM PUBLIC.REVIEWS r " +
                    "LEFT JOIN REVIEWLIKES rl ON rl.REVIEWID = r.ID " +
                    "GROUP BY R.ID " +
                    "ORDER BY useful desc " +
                    "LIMIT ?;";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, count);
        } else {
            String sqlQuery = "SELECT R.ID, " +
                    "r.CONTENT, " +
                    "r.ISPOSITIVE, " +
                    "r.USERID, " +
                    "r.FILMID,  " +
                    "COUNT(NULLIF(POSITIVE, false))-COUNT(NULLIF(POSITIVE,true)) useful " +
                    "FROM PUBLIC.REVIEWS r " +
                    "LEFT JOIN REVIEWLIKES rl ON rl.REVIEWID = r.ID " +
                    "WHERE FILMID = ? " +
                    "GROUP BY R.ID " +
                    "ORDER BY useful desc " +
                    "LIMIT ?;";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, filmId, count);
        }
        log.debug("Review на Film c ID {} получены.", filmId);
        return reviews;
    }

//    @Override
//    public void saveReviewLikesOrDislikes(Long reviewId, Long userId, boolean positive) {
//        String sql = "DELETE FROM REVIEWLIKES WHERE REVIEWID = ? AND USERID = ? ";
//        jdbcTemplate.update(sql, reviewId, userId);
//        sql = "INSERT INTO reviewLikes (REVIEWID, USERID, POSITIVE) VALUES(?, ?, ?) ";
//        jdbcTemplate.update(sql, reviewId, userId, positive);
//        log.debug("Реакция на Review c ID {} от User c ID {} сохранены", reviewId, userId);
//    }

    @Override
    public void saveReviewLikesOrDislikes(Long reviewId, Long userId, boolean positive) {
        String sql = "MERGE INTO reviewLikes AS RL " +
                "USING (SELECT CAST(? AS BIGINT) AS REVIEWID, CAST(? AS BIGINT) AS USERID, CAST(? AS BOOLEAN) AS POSITIVE) AS TMP " +
                "ON (RL.REVIEWID = TMP.REVIEWID AND RL.USERID = TMP.USERID) " +
                "WHEN MATCHED THEN " +
                "    UPDATE SET RL.POSITIVE = TMP.POSITIVE " +
                "WHEN NOT MATCHED THEN " +
                "    INSERT (REVIEWID, USERID, POSITIVE) VALUES(TMP.REVIEWID, TMP.USERID, TMP.POSITIVE);";
        jdbcTemplate.update(sql, reviewId, userId, positive);
        log.debug("Реакция на Review c ID {} от User c ID {} сохранены", reviewId, userId);
    }



    @Override
    public void deleteReviewLikesOrDislikes(Long reviewId, Long userId, boolean positive) {
        String sql = "DELETE FROM PUBLIC.REVIEWLIKES " +
                "WHERE USERID=? AND REVIEWID=? AND POSITIVE=?";
        jdbcTemplate.update(sql, positive, userId, reviewId);
        log.debug("Удаление реакции на Review c ID {} от User c ID {} успешно выполнено", reviewId, userId);
    }

    @Override
    public Review updateReview(Review review) {
        //Здесь только 2 параметра обновляется, иначе не проходит постмен тесты.
        // Пояснения в посте наставника: https://app.pachca.com/chats?thread_id=1280887
        String sqlQuery = "UPDATE PUBLIC.REVIEWS  " +
                "SET CONTENT = ?, ISPOSITIVE = ?  " +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        Review reviewById = getReviewById(review.getReviewId());
        log.debug("Review c ID {} обновлен", review.getReviewId());
        return reviewById;
    }


    @Override
    public void deleteReviewById(Long id) {
        String sqlQuery = "DELETE FROM PUBLIC.REVIEWS WHERE ID=? ";
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Review c ID {} удалён", id);
    }

    public static Review mapToReview(ResultSet resultSet, int i) {
        Review build = null;
        try {
            build = Review.builder()
                    .reviewId(resultSet.getLong("id"))
                    .content(resultSet.getString("content"))
                    .filmId(resultSet.getLong("filmId"))
                    .userId(resultSet.getLong("userId"))
                    .isPositive(resultSet.getBoolean("isPositive"))
                    .build();
            build.setUseful(resultSet.getInt("useful"));
        } catch (SQLException ignored) {
            assert build != null;
            build.setUseful(0);
        }
        return build;
    }

    public Map<String, ?> reviewToMap(Review review) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("id", review.getReviewId());
        temp.put("content", review.getContent());
        temp.put("ispositive", review.getIsPositive());
        temp.put("userid", review.getUserId());
        temp.put("filmid", review.getFilmId());
        return temp;
    }

}
