import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FooTest {

    private ISuffixService suffixService;

    @Before
    public void setUp() {
        suffixService = mock(ISuffixService.class);
    }

    @Test
    public void appendToUsesServiceToGetSuffix() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(anyInt())).thenReturn("cod");

        assertThat(foo.appendTo("ing")).isEqualTo("coding");
    }

    @Test
    public void appendToUsesLengthOfRoot() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(4)).thenReturn("co");

        assertThat(foo.appendTo("ding")).isEqualTo("coding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendToExpectsNonNullArgument() {
        new Foo(suffixService).appendTo(null);
    }

    static class Foo {
        private final ISuffixService suffixService;

        public Foo(ISuffixService suffixService) {
            this.suffixService = suffixService;
        }

        public String appendTo(String root) {
            final String nonNullRoot = Optional.ofNullable(root)
                    .orElseThrow(() -> new IllegalArgumentException("root cannot be null"));
            final String suffix = suffixService.getSuffix(nonNullRoot.length());

            return suffix + nonNullRoot;
        }
    }

    interface ISuffixService {
        String getSuffix(int length);
    }
}
