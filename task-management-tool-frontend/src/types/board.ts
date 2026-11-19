export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

export interface Task {
  id: number;
  title: string;
  description: string | null;
  status: TaskStatus;
  position: number;
  listId: number;
}

export interface TaskList {
  id: number;
  title: string;
  position: number;
  tasks: Task[];
}

export interface Board {
  id: number;
  title: string;
  workspaceId: number;
  lists: TaskList[];
}

export interface UpdateTaskPayload {
  title?: string;
  description?: string;
  status?: TaskStatus;
  targetListId?: number;
  position?: number;
}
