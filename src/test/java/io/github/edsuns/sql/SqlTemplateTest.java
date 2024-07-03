package io.github.edsuns.sql;

import io.github.edsuns.sql.entity.Book;
import io.github.edsuns.sql.protocol.ReadStatement;
import io.github.edsuns.sql.protocol.Sql;
import io.github.edsuns.sql.protocol.WriteStatement;
import io.github.edsuns.sql.query.BookQuery;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.edsuns.sql.SqlTemplates.select;
import static io.github.edsuns.sql.SqlTemplates.update;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:50
 */
class SqlTemplateTest {

    @Test
    void testSelectWhere() {
        BookQuery query = new BookQuery();

        ReadStatement<Book, BookQuery, Book> select1 = select(Book.class, BookQuery.class)
                .whereSelective()
                .where(x -> x.equals(Book::getName, BookQuery::getName))
                .onlyOne();
        assertEquals("SELECT `id`,`name`,`price` FROM `book`",
                select1.generateSql(query).getSqlTemplateString());

        ReadStatement<Book, BookQuery, Book> select2 = select(Book.class, BookQuery.class)
                .whereSelective()
                .where(x -> x.equals(Book::getName, o -> "test"))
                .onlyOne();
        assertEquals("SELECT `id`,`name`,`price` FROM `book` WHERE `name`=?",
                select2.generateSql(query).getSqlTemplateString());
    }

    @Test
    void testFullSelectAndUpdate() {
        WriteStatement<Book, BookQuery, Long> update = update(Book.class, BookQuery.class).ignore().lowPriority()
                .set(x -> x.assign(Book::getId, o -> 1)
                        .assign(Book::getName, BookQuery::getName)
                        .assign(Book::getPrice, SqlTemplates::nullValue))
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike)
                )
                .limit(o -> 1)
                .affected();
        ReadStatement<Book, BookQuery, List<Book>> select = select(Book.class, BookQuery.class).distinct()
                .whereSelective()
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike).or().equals(Book::getPrice, BookQuery::getPrice)
                        .or(y -> y.equals(Book::getName, BookQuery::getName))
                )
                .groupBy(Arrays.asList(Book::getName, Book::getPrice), having -> having.equals(Book::getName, BookQuery::getName))
                .orderBy(x -> x.asc(Book::getId))
                .limit(BookQuery::getLimit)
                .list();
        ReadStatement<Book, BookQuery, Long> selectCount = select(Book.class, BookQuery.class)
                .whereSelective()
                .limit(BookQuery::getLimit)
                .count();

        BookQuery query = new BookQuery();
        query.setId(1L);
        query.setName("test");
        query.setNameLike("test");
        query.setNames(Arrays.asList("1", "2", "3"));
        query.setLimit(15L);
        assertEquals("UPDATE LOW_PRIORITY IGNORE `book` SET `id`=?, `name`=?, `price`=NULL WHERE `name` IN (?,?,?) AND `name` LIKE ?",
                update.generateSql(null, query).getSqlTemplateString());
        assertEquals("SELECT DISTINCT `id`,`name`,`price` FROM `book` WHERE (`name` IN (?,?,?) AND `name` LIKE ? OR (`name`=?))" +
                        " AND (`id`=? AND `name`=?) GROUP BY `name`,`price` HAVING `name`=? ORDER BY `id` LIMIT 15",
                select.generateSql(query).getSqlTemplateString());
        assertEquals("SELECT COUNT(1) FROM `book` WHERE `id`=? AND `name`=? LIMIT 15",
                selectCount.generateSql(query).getSqlTemplateString());
    }

    @Test
    void selectAllFieldRequired() {
        BookQuery query = new BookQuery();
        query.setId(1L);
        query.setNameLike("test");
        query.setNames(Arrays.asList("1", "2", "3"));
        query.setLimit(15L);
        ReadStatement<Book, BookQuery, List<Book>> select = select(Book.class, BookQuery.class).distinct()
                .whereSelective()
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike).or().equals(Book::getPrice, BookQuery::getPrice)
                        .or(y -> y.equals(Book::getName, BookQuery::getName))
                )
                .groupBy(Arrays.asList(Book::getName, Book::getPrice), having -> having.equals(Book::getName, BookQuery::getName))
                .orderBy(x -> x.asc(Book::getId))
                .limit(BookQuery::getLimit)
                .list();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> select.generateSql(query));
        assertEquals("field required in query object: `name`", ex.getMessage());
        query.setName("test");
        assertEquals("SELECT DISTINCT `id`,`name`,`price` FROM `book` WHERE (`name` IN (?,?,?) AND `name` LIKE ? OR (`name`=?))" +
                        " AND (`id`=? AND `name`=?) GROUP BY `name`,`price` HAVING `name`=? ORDER BY `id` LIMIT 15",
                select.generateSql(query).getSqlTemplateString());
    }

    @Test
    void updateSelective() {
        BookQuery query = new BookQuery();
        query.setName("test");
        query.setNameLike("test");
        query.setNames(Arrays.asList("1", "2", "3"));
        WriteStatement<Book, BookQuery, Long> updateSelective = update(Book.class, BookQuery.class).ignore().lowPriority()
                .setSelective()
                .set(x -> x.assign(Book::getName, BookQuery::getName))
                .where(x -> x
                        .in(Book::getName, BookQuery::getNames)
                        .like(Book::getName, BookQuery::getNameLike)
                )
                .limit(o -> 1)
                .affected();
        Book entity = new Book();
        entity.setId(2L);
        entity.setPrice(null);
        Sql sql = updateSelective.generateSql(entity, query);
        assertEquals("UPDATE LOW_PRIORITY IGNORE `book` SET `id`=?, `name`=? WHERE `name` IN (?,?,?) AND `name` LIKE ?",
                sql.getSqlTemplateString());
        assertEquals(6, sql.getVariables().size());
        assertEquals(new ArrayList<>(Arrays.asList(2L, "test", "1", "2", "3", "%test%")), new ArrayList<>(sql.getVariables()));
    }
}