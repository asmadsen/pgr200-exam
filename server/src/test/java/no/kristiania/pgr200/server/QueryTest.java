package no.kristiania.pgr200.server;

import no.kristiania.pgr200.server.query.SqlOperator;
import no.kristiania.pgr200.server.query.Query;
import no.kristiania.pgr200.server.models.TalkModel;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@Ignore
public class QueryTest {
    @Ignore
    @Test
    public void shouldBuildFindBy() {
        Query query = new Query<TalkModel>("tableName");
        query.findBy(TalkModel.class,12);
        assertEquals("SELECT id, title, description, topic FROM tableName WHERE id = ?;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldCombineWhereWithAND(){
         Query query = new Query<TalkModel>("tableName");
         query.where("title", SqlOperator.Like, "some title")
                 .where("topic", SqlOperator.Like, "java");
         assertEquals("SELECT * FROM tableName WHERE title LIKE ? AND topic LIKE ?;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildWhereNotNull(){
        Query query = new Query<TalkModel>("tableName");
        query.whereNot("title", SqlOperator.Null.getOperator());
        assertEquals("SELECT * FROM tableName WHERE title NOT ?;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildWhereNull(){
        Query query = new Query<TalkModel>("tableName");
        query.whereIsNull("title");
        assertEquals("SELECT * FROM tableName WHERE title IS NULL;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildSelect(){
        Query query = new Query<TalkModel>("tableName");
        query.select("column1", "column2", "column3", "column4");
        assertEquals("SELECT column1, column2, column3, column4 FROM tableName;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildDefaultStatement(){
        Query query = new Query<TalkModel>("tableName");
        assertEquals("SELECT * FROM tableName;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildWhereEquals(){
        Query query = new Query<TalkModel>("tableName");
        query.whereEquals("title", "java talk");
        assertEquals("SELECT * FROM tableName WHERE title = ?;", query.buildSql());
    }

    @Ignore
    @Test
    public void shouldBuildInsertStatement(){
        Query query = new Query("tableName");
        HashMap<String, String> map = new HashMap<>();
        map.put("title", "Java talks");
    }
}
