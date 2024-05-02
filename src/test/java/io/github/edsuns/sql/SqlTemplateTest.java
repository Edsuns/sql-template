package io.github.edsuns.sql;

import io.github.edsuns.sql.entity.Book;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.query.BookQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.github.edsuns.sql.SqlTemplates.select;
import static io.github.edsuns.sql.SqlTemplates.update;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:50
 */
class SqlTemplateTest {

    @Test
    void testSelectWhere() {
        Book entity = new Book();
        BookQuery query = new BookQuery();

        SqlTemplate<Book, BookQuery, Book> select1 = select(Book.class, BookQuery.class)
                .whereSelective()
                .where(x -> x.equals(Book::getName, BookQuery::getName))
                .onlyOne();
        assertEquals("SELECT `id`,`name`,`price` FROM `book`",
                select1.generateSql(entity, query).getSqlTemplateString());

        SqlTemplate<Book, BookQuery, Book> select2 = select(Book.class, BookQuery.class)
                .whereSelective()
                .where(x -> x.equals(Book::getName, o -> "test"))
                .onlyOne();
        assertEquals("SELECT `id`,`name`,`price` FROM `book` WHERE `name`=?",
                select2.generateSql(entity, query).getSqlTemplateString());
    }

    @Test
    void testFullSelectAndUpdate() {
        SqlTemplate<Book, BookQuery, Long> update = update(Book.class, BookQuery.class).ignore().lowPriority()
                .set(x -> x.assign(Book::getId, o -> 1)
                        .assign(Book::getName, BookQuery::getName)
                        .assign(Book::getPrice, SqlTemplates::nullValue))
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike).or().equals(Book::getPrice, BookQuery::getPrice)
                        .or(y -> y.equals(Book::getPrice, BookQuery::getPrice))
                )
                .limit(o -> 1)
                .affected();
        SqlTemplate<Book, BookQuery, List<Book>> select = select(Book.class, BookQuery.class).distinct()
                .whereSelective()
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike).or().equals(Book::getPrice, BookQuery::getPrice)
                        .or(y -> y.equals(Book::getName, BookQuery::getName).equals(Book::getPrice, BookQuery::getPrice))
                )
                .groupBy(Arrays.asList(Book::getName, Book::getPrice), having -> having.equals(Book::getName, BookQuery::getName))
                .orderBy(x -> x.asc(Book::getId))
                .limit(BookQuery::getLimit)
                .list();
        SqlTemplate<Book, BookQuery, Long> selectCount = select(Book.class, BookQuery.class)
                .whereSelective()
                .limit(BookQuery::getLimit)
                .count();

        Book entity = new Book();
        BookQuery query = new BookQuery();
        query.setId(1L);
        query.setName("test");
        query.setNameLike("test");
        query.setNames(Arrays.asList("1", "2", "3"));
        query.setLimit(15L);
        assertEquals("UPDATE LOW_PRIORITY IGNORE `book` SET `id`=?, `name`=?, `price`=NULL WHERE `name` IN (?,?,?) AND `name` LIKE ?",
                update.generateSql(entity, query).getSqlTemplateString());
        assertEquals("SELECT DISTINCT `id`,`name`,`price` FROM `book` WHERE (`name` IN (?,?,?) AND `name` LIKE ? OR (`name`=?))" +
                        " AND (`id`=? AND `name`=?) GROUP BY `name`,`price` HAVING `name`=? ORDER BY `id` LIMIT 15",
                select.generateSql(entity, query).getSqlTemplateString());
        assertEquals("SELECT COUNT(1) FROM `book` WHERE `id`=? AND `name`=? LIMIT 15",
                selectCount.generateSql(entity, query).getSqlTemplateString());
    }
}