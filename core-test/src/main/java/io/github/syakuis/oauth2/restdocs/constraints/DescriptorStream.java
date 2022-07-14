package io.github.syakuis.oauth2.restdocs.constraints;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */
public class DescriptorStream {
    private Stream<Descriptor> stream;

    DescriptorStream(Stream<Descriptor> stream) {
        this.stream = stream;
    }

    public Stream<Descriptor> stream() {
        return stream;
    }

    public DescriptorStream exclude(Descriptor... descriptor) {

        if (descriptor == null || descriptor.length < 1) {
            throw new IllegalArgumentException("descriptor argument must have length; it must not be null or empty");
        }

        List<String> names = Arrays.stream(descriptor).map(Descriptor::name).toList();
        this.stream = stream.filter(it -> !names.contains(it.name()));

        return this;
    }

    public <T> List<T> collect(DescriptorCollectors<T> collectors) {
        return collectors.toList(stream);
    }
}