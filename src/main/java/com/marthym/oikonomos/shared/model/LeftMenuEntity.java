package com.marthym.oikonomos.shared.model;

import com.marthym.oikonomos.shared.view.data.EntityType;

public abstract class LeftMenuEntity {
	public abstract long getEntityId();
	public abstract String getEntityOwner();
	public abstract String getEntityDescription();
	public abstract EntityType getEntityType();
}
