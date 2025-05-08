package cycleguard.database.cache;

import cycleguard.database.AbstractDatabaseEntry;

public class CacheTimeToDelete<EntryType> {
	private long timeToDelete;
	private EntryType entry;
	private boolean isDirty;
	private int timesWritten;


	public CacheTimeToDelete(long timeToDelete, EntryType entry, boolean isDirty) {
		this.timeToDelete = timeToDelete;
		this.entry = entry;
		this.isDirty = isDirty;
		if (isDirty) timesWritten=1;
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

	public void incrementTimesWritten() {
		timesWritten++;
	}

	public boolean doWriteOverride() {
		return timesWritten>=DatabaseCacheService.MAX_CACHE_WRITES;
	}

	public void resetTimesWritten() {
		timesWritten=0;
		isDirty=false;
	}
}
