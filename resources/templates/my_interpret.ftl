[#ftl /]

[#if myContent!?trim?has_content]
  [#assign myTemplate = myContent?interpret /]
  [@myTemplate /]
[/#if]
