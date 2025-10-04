package vn.ghtk.demo.catalog.application.port.in.mp.input;

import vn.ghtk.demo.catalog.domain.attribute.AttributeValue;
import vn.ghtk.demo.catalog.domain.mp.MasterProductId;
import vn.ghtk.demo.catalog.domain.mp.VariantCodeType;

import java.util.List;

public record AddMasterVariantCmd(MasterProductId masterProductId,
                                  List<AttributeValue> attributeValues,
                                  String name,
                                  String codeValue,
                                  VariantCodeType codeType) {
}
