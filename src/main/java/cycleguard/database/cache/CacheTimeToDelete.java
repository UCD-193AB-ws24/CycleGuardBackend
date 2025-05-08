package cycleguard.database.cache;

import cycleguard.database.AbstractDatabaseEntry;

public class CacheTimeToDelete<EntryType> {

	private long timeToDelete;
	private EntryType entry;
	private boolean isDirty = false;

	public CacheTimeToDelete(long timeToDelete, EntryType entry, boolean isDirty) {
		this.timeToDelete = timeToDelete;
		this.entry = entry;
		this.isDirty = isDirty;
	}

	public long getTimeToDelete() {
		return timeToDelete;
	}

	public void setTimeToDelete(long timeToDelete) {
		this.timeToDelete = timeToDelete;
	}

	public EntryType getEntry() {
		return entry;
	}

	public void setEntry(EntryType entry) {
		this.entry = entry;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}
}
