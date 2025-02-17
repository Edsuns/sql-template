# sql-template

## features

- [x] entity-query-objects database access design
- [x] `SqlTemplate` as a static field
- [x] `onlyOne`, `list`, `affected` result mapping
- [x] lambda/static-value where conditions
- [x] lambda/static-value update set conditions
- [x] composition of statements
- [x] select specify columns
- [x] selective where conditions
- [x] selective set statements
- [x] optional selective
- [ ] join statements

example:

```java
public class BookQuery implements Query {
    private String name;
    private String descriptionLike;
    // getters... setters...
}

public interface BookDatabase {
    ReadStatement<Book, BookQuery, List<Book>> LIST = select(Book.class, BookQuery.class)
            .whereSelective()
            .where(x -> x.like(Book::getDescription, BookQuery::getDescriptionLike))
            .list();
    WriteStatement<Book, BookQuery, Long> UPDATE = update(Book.class, BookQuery.class)
            .setSelective()
            .where(x -> x.equals(Book::getName, BookQuery::getName, /* selective */ true))
            .affected();
    // select specify columns
    ReadStatement<Book, BookQuery, List<Book>> LIST_BY_DESC_LIKE =
            select(Book.class, BookQuery.class, singletonList(Book::getId))
                    .whereSelective()
                    .list();
    // composition of statements
    // here we use the same where conditions in two statements for demonstration
    Consumer<Where<Book, BookQuery>> BY_DESC_LIKE =
            x -> x.equals(Book::getDescription, BookQuery::getDescriptionLike);
    ReadStatement<Book, BookQuery, List<Book>> LIST_ID_BY_DESC_LIKE = select(Book.class, BookQuery.class)
            .where(BY_DESC_LIKE)
            .list();
    WriteStatement<Book, BookQuery, Long> UPDATE_BY_DESC_LIKE = update(Book.class, BookQuery.class)
            .where(BY_DESC_LIKE)
            .affected();
}

public class BookBusiness {
    public List<Book> list(BookQuery query) {
        return BookDatabase.LIST.execute(query);
    }
    public List<Book> update(Book book, BookQuery query) {
        return BookDatabase.UPDATE.execute(book, query);
    }
}

class BookBusinessTest {
    private final BookBusiness bookBusiness = new BookBusiness();

    @BeforeAll
    static void beforeAll() {
        // initialize database...
    }

    @Test
    void testListByName() {
        BookQuery query = new BookQuery();
        query.setName("book name");
        List<Book> books = bookBusiness.list(query);
        assertEquals("book name", books.get(0).getName());
    }

    @Test
    void testSearchByDescription(String description) {
        BookQuery query = new BookQuery();
        query.setDescriptionLike("description text");
        List<Book> books = bookBusiness.list(query);
        assertTrue(books.get(0).getDescription().contains("description text"));
    }

    @Test
    void testUpdateDescByName() {
        BookQuery query = new BookQuery();
        query.setName("book name");
        Book entity = new Book();
        entity.setDescription("new desc");
        Long affected = bookBusiness.update(entity, query);
        assertEquals(1L, affected);
    }
}
```
