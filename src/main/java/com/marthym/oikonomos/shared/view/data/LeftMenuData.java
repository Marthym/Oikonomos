package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LeftMenuData implements HasEntityCountData, Serializable {
	private static final long serialVersionUID = -5182533175212903165L;

	private Map<EntityType, Integer> entityCount = new HashMap<EntityType, Integer>();

	@Override
	public int getCountFor(EntityType entityName) {
		if (entityCount.get(entityName) != null)
			return entityCount.get(entityName);
		else
			return 0;
	}
	
	public void addEntityCount(EntityType entity, int count) {
		entityCount.put(entity, new Integer(count));
	}
}
