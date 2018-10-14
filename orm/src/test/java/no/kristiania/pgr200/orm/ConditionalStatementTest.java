package no.kristiania.pgr200.orm;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class ConditionalStatementTest {

    @Test
    public void shouldBuildSqlStatement(){
        ConditionalStatement statement = new ConditionalStatement("id", SqlOperator.Equals, 15);
        assertEquals("`id` = ?", statement.getSqlStatement());
    }

    @Test
    public void shouldPrependAndToStatement(){
         ConditionalStatement statement = new ConditionalStatement("users.id", SqlOperator.Like, 123);
         assertEquals("`users`.`id` LIKE ? AND `users`.`id` LIKE ?", statement.getSqlStatement(statement.getSqlStatement()));
    }

    @Test
    public void shouldPrependOrToStatement(){
        ConditionalStatement statement = new ConditionalStatement("users.id", SqlOperator.Equals, 123, false);
        assertEquals("`users`.`id` = ? OR `users`.`id` = ?", statement.getSqlStatement(statement.getSqlStatement()));
    }

    @Test
    public void shouldCombineAllStatements(){
        ConditionalStatement statement1 = new ConditionalStatement("users.id", SqlOperator.Like, 1);
        ConditionalStatement statement2 = new ConditionalStatement("users.id", SqlOperator.Equals, 2, false);
        ConditionalStatement statement3 = new ConditionalStatement("users.id", SqlOperator.Equals, 3, false);
        LinkedList<ConditionalStatement> list = new LinkedList<>();

        list.add(statement1);
        list.add(statement2);
        list.add(statement3);

        assertEquals("WHERE `users`.`id` LIKE ? OR `users`.`id` = ? OR `users`.`id` = ?", ConditionalStatement.buildStatements(list));
    }
}
