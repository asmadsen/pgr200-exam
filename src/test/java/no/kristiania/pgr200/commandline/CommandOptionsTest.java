package no.kristiania.pgr200.commandline;

import com.github.javafaker.Faker;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


public class CommandOptionsTest {
    private Faker faker = new Faker();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldParseOptionsArray() {
        String title = faker.book().title();
        String description = faker.lorem().paragraph(2);
        CommandOptions options = new CommandOptions(new String[]{"-title", title, "-description", description});
        assertEquals(title, options.get("title"));
        assertEquals(description, options.get("description"));
        exception.expect(RuntimeException.class);
        options.get("unavilable-key"); // Test that throws exception if trying to access non existent option key
    }
}