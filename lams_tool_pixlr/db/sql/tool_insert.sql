-- CVS ID: $Id$
 
INSERT INTO lams_tool
(
tool_signature,
service_name,
tool_display_name,
description,
tool_identifier,
tool_version,
learning_library_id,
default_tool_content_id,
valid_flag,
grouping_support_type_id,
supports_run_offline_flag,
learner_url,
learner_preview_url,
learner_progress_url,
author_url,
monitor_url,
define_later_url,
export_pfolio_learner_url,
export_pfolio_class_url,
contribute_url,
moderation_url,
help_url,
language_file,
classpath_addition,
context_file,
create_date_time,
modified_date_time,
supports_outputs
)
VALUES
(
'lapixl10',
'pixlrService',
'Pixlr',
'Pixlr',
'pixlr',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/lapixl10/learning.do?mode=learner',
'tool/lapixl10/learning.do?mode=author',
'tool/lapixl10/learning.do?mode=teacher',
'tool/lapixl10/authoring.do',
'tool/lapixl10/monitoring.do',
'tool/lapixl10/authoring.do?mode=teacher',
'tool/lapixl10/exportPortfolio?mode=learner',
'tool/lapixl10/exportPortfolio?mode=teacher',
'tool/lapixl10/contribute.do',
'tool/lapixl10/moderate.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lapixl10',
'org.lamsfoundation.lams.tool.pixlr.ApplicationResources',
'lams-tool-lapixl10.jar',
'/org/lamsfoundation/lams/tool/pixlr/pixlrApplicationContext.xml',
NOW(),
NOW(),
0
)
