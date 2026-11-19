import type { Board, Task, TaskList, UpdateTaskPayload } from '../types/board';

const API_BASE = 'http://localhost:8080/api';

export async function fetchBoard(boardId: number): Promise<Board> {
  const res = await fetch(`${API_BASE}/boards/${boardId}`);
  if (!res.ok) throw new Error('Failed to load board');
  return res.json();
}

export async function createList(boardId: number, title: string): Promise<TaskList> {
  const res = await fetch(`${API_BASE}/boards/${boardId}/lists`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title }),
  });
  if (!res.ok) throw new Error('Failed to create list');
  return res.json();
}

export async function createTask(listId: number, title: string, description: string): Promise<Task> {
  const res = await fetch(`${API_BASE}/lists/${listId}/tasks`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title, description }),
  });
  if (!res.ok) throw new Error('Failed to create task');
  return res.json();
}

export async function updateTask(taskId: number, payload: UpdateTaskPayload): Promise<Task> {
  const res = await fetch(`${API_BASE}/tasks/${taskId}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error('Failed to update task');
  return res.json();
}

export async function deleteTask(taskId: number): Promise<void> {
  const res = await fetch(`${API_BASE}/tasks/${taskId}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Failed to delete task');
}
