import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FooTest {

    @Test
    public void appendToReturnsValue() {
        final Foo foo = new Foo();

        assertThat(foo.appendTo("")).isNotNull();
    }

    static class Foo {
        public String appendTo(String root) {
            return "";
        }
    }
}
