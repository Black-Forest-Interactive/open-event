package de.sambalmueslie.openevent.core.issue

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.issue.api.Issue

interface IssueChangeListener : BusinessObjectChangeListener<Long, Issue>
