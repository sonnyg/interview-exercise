import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FooTest {

    @Test
    public void appendToReturnsValue() {
        final Foo foo = new Foo("cod");

        assertThat(foo.appendTo("ing")).isEqualTo("coding");
    }

    @Test
    public void fooInstancesCanHaveDifferentSuffixes() {
        new Foo("foo");
        new Foo("bar");
    }

    static class Foo {
        private final String _suffix;

        public Foo(String _suffix) {
            this._suffix = _suffix;
        }

        public String appendTo(String root) {
            return _suffix + root;
        }
    }
}
