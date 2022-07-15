package io.github.syakuis.oauth2.restdocs.constraints;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */
public interface DescriptorCollectors<T> {
    List<T> toList(Stream<Descriptor> stream);

    static List<HeaderDescriptor> headerDescriptor(Stream<Descriptor> stream) {
        return stream.map(it -> {
            HeaderDescriptor descriptor = headerWithName(it.name()).description(it.getDescription());

            if (it.isOptional()) {
                descriptor.optional();
            }

            return descriptor;
        }).collect(Collectors.toList());
    }

    static List<LinkDescriptor> linkDescriptor(Stream<Descriptor> stream) {
        return stream.map(it -> {
            LinkDescriptor descriptor = linkWithRel(it.name()).description(it.getDescription());

            if (it.isOptional()) {
                descriptor.optional();
            }

            return descriptor;
        }).collect(Collectors.toList());
    }

    static List<FieldDescriptor> fieldDescriptor(Stream<Descriptor> stream) {
        return stream.map(it -> {
            FieldDescriptor descriptor = fieldWithPath(it.name()).description(it.getDescription());

            if (it.isOptional()) {
                descriptor.optional();
            }

            return descriptor;
        }).collect(Collectors.toList());
    }

    static List<RequestPartDescriptor> requestPartDescriptor(Stream<Descriptor> stream) {
        return stream.map(it -> {
            RequestPartDescriptor descriptor = partWithName(it.name()).description(it.getDescription());

            if (it.isOptional()) {
                descriptor.optional();
            }

            return descriptor;
        }).collect(Collectors.toList());
    }


    static List<ParameterDescriptor> parameterDescriptor(Stream<Descriptor> stream) {
        return stream.map(it -> {
            ParameterDescriptor descriptor = parameterWithName(it.name()).description(it.getDescription());

            if (it.isOptional()) {
                descriptor.optional();
            }

            return descriptor;
        }).collect(Collectors.toList());
    }
}