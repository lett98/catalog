package vn.ghtk.demo.catalog.domain.common.id;

public class IdGeneratorFactory {

    public static IdGenerator<Integer> integerIdGenerator() {
        return new SequenceIdGenerator();
    }

}
