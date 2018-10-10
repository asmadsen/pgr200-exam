package no.kristiania.pgr200.server;

import no.kristiania.pgr200.server.models.Operator;
import no.kristiania.pgr200.server.models.Query;
import no.kristiania.pgr200.server.models.Statement;
import no.kristiania.pgr200.server.models.Talk;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class QueryTest {
    @Test
    public void shouldBuildFindBy() {
        Query query = new Query<Talk>("tableName");
        query.findBy(Talk.class,12);
        assertEquals("SELECT id, title, description, topic FROM tableName WHERE id = ?;", query.buildSql());
    }

    @Test
    public void shouldCombineWhereWithAND(){
         Query query = new Query<Talk>("tableName");
         query.where("title", Operator.Like, "some title")
                 .where("topic", Operator.Like, "java");
         assertEquals("SELECT * FROM tableName WHERE title LIKE ? AND topic LIKE ?;", query.buildSql());
    }

    @Test
    public void shouldBuildWhereNotNull(){
        Query query = new Query<Talk>("tableName");
        query.whereNot("title", Operator.Null.getOperator());
        assertEquals("SELECT * FROM tableName WHERE title NOT ?;", query.buildSql());
    }

    @Test
    public void shouldBuildWhereNull(){
        Query query = new Query<Talk>("tableName");
        query.whereIsNull("title");
        assertEquals("SELECT * FROM tableName WHERE title IS NULL;", query.buildSql());
    }

    @Test
    public void shouldBuildSelect(){
        Query query = new Query<Talk>("tableName");
        query.select("column1", "column2", "column3", "column4");
        assertEquals("SELECT column1, column2, column3, column4 FROM tableName;", query.buildSql());
    }

    @Test
    public void shouldBuildDefaultStatement(){
        Query query = new Query<Talk>("tableName");
        assertEquals("SELECT * FROM tableName;", query.buildSql());
    }

    @Test
    public void shouldBuildWhereEquals(){
        Query query = new Query<Talk>("tableName");
        query.whereEquals("title", "java talk");
        assertEquals("SELECT * FROM tableName WHERE title = ?;", query.buildSql());
    }

    @Test
    public void shouldBuildInsertStatement(){
        Query query = new Query("tableName");
        HashMap<String, String> map = new HashMap<>();
        map.put("title", "Java talks");
        query.insert(map);
        assertThat()
    }
}
