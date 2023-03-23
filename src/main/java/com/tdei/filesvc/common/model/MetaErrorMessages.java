package com.tdei.filesvc.common.model;

public interface MetaErrorMessages {

    String INVALID_DATA_SOURCE = "data_source should be one of the following `3rdParty`,`TDEITools`,`InHouse`";
    String INVALID_COLLECTION_METHOD = "collection_method needs to be one of the following `manual`,`transform`,`generated`,`other`";

    String COLLECTION_DATE_FUTURE = "collection_date cannot be future";

    String NO_COLLECTION_DATE = "collection_date is invalid";

    String COLLECTED_BY_LENGTHY = "collected_by should be less than 50 characters";
}
