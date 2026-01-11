[#ftl /]

[#if dynamicContent!?trim?has_content]
  [#assign myTemplate = dynamicContent?interpret /]
  [@myTemplate /]
[/#if]
