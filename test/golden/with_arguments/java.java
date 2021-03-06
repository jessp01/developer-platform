KalturaMediaEntryFilter filter = new KalturaMediaEntryFilter();
filter.nameLike = "foobar";
filter.statusEqual = KalturaEntryStatus.READY;
filter.orderBy = KalturaMediaEntryOrderBy.CREATED_AT_ASC;
filter.advancedSearch = new KalturaAttributeCondition();
filter.advancedSearch.value = "baz";
KalturaFilterPager pager = new KalturaFilterPager();
pager.pageSize = 3;

Object result = client.getMediaService().list(filter, pager);
System.out.println(result);