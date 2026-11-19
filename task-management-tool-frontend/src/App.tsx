import { useEffect, useState } from 'react';
import type { Board, Task, TaskList, TaskStatus } from './types/board';
import { createList, createTask, deleteTask, fetchBoard, updateTask } from './api/boardApi';
import './App.css';

export function App() {
  const [board, setBoard] = useState<Board | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // New List state
  const [newListTitle, setNewListTitle] = useState('');

  // New Task state (per list)
  const [taskTitles, setTaskTitles] = useState<Record<number, string>>({});
  const [taskDescriptions, setTaskDescriptions] = useState<Record<number, string>>({});

  const loadBoard = async () => {
    try {
      setError(null);
      const data = await fetchBoard(1);
      setBoard(data);
    } catch (err) {
      setError('Could not connect to backend. Make sure Spring Boot is running on port 8080.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBoard();
  }, []);

  const handleAddList = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!board || !newListTitle.trim()) return;
    await createList(board.id, newListTitle.trim());
    setNewListTitle('');
    await loadBoard();
  };

  const handleAddTask = async (e: React.FormEvent, listId: number) => {
    e.preventDefault();
    const title = taskTitles[listId]?.trim();
    if (!title) return;
    const description = taskDescriptions[listId]?.trim() || '';

    await createTask(listId, title, description);
    setTaskTitles((prev) => ({ ...prev, [listId]: '' }));
    setTaskDescriptions((prev) => ({ ...prev, [listId]: '' }));
    await loadBoard();
  };

  const handleMoveTask = async (task: Task, targetList: TaskList) => {
    // Infer status from column title when moving cards
    let newStatus: TaskStatus = task.status;
    const lower = targetList.title.toLowerCase();
    if (lower.includes('done')) newStatus = 'DONE';
    else if (lower.includes('progress')) newStatus = 'IN_PROGRESS';
    else if (lower.includes('to-do') || lower.includes('todo')) newStatus = 'TODO';

    await updateTask(task.id, {
      targetListId: targetList.id,
      status: newStatus,
      position: targetList.tasks.length,
    });
    await loadBoard();
  };

  const handleDeleteTask = async (taskId: number) => {
    await deleteTask(taskId);
    await loadBoard();
  };

  if (loading) return <div className="status-screen">Loading workspace...</div>;
  if (error || !board) return <div className="status-screen error">{error}</div>;

  return (
    <div className="app-container">
      <header className="board-header">
        <h1>{board.title}</h1>
        <form onSubmit={handleAddList} className="add-list-form">
          <input
            type="text"
            placeholder="+ Add new column..."
            value={newListTitle}
            onChange={(e) => setNewListTitle(e.target.value)}
          />
          <button type="submit">Add List</button>
        </form>
      </header>

      <main className="board-columns">
        {board.lists.map((list, listIndex) => (
          <section key={list.id} className="column">
            <div className="column-header">
              <h2>{list.title}</h2>
              <span className="badge">{list.tasks.length}</span>
            </div>

            <div className="task-list">
              {list.tasks.map((task) => (
                <article key={task.id} className="task-card">
                  <div className="task-card-top">
                    <span className={`status-pill ${task.status.toLowerCase()}`}>
                      {task.status.replace('_', ' ')}
                    </span>
                    <button
                      className="delete-btn"
                      onClick={() => handleDeleteTask(task.id)}
                      title="Delete task"
                    >
                      ×
                    </button>
                  </div>

                  <h3>{task.title}</h3>
                  {task.description && <p className="task-desc">{task.description}</p>}

                  <div className="task-actions">
                    {listIndex > 0 && (
                      <button
                        className="move-btn"
                        onClick={() => handleMoveTask(task, board.lists[listIndex - 1])}
                      >
                        ← {board.lists[listIndex - 1].title}
                      </button>
                    )}
                    {listIndex < board.lists.length - 1 && (
                      <button
                        className="move-btn"
                        onClick={() => handleMoveTask(task, board.lists[listIndex + 1])}
                      >
                        {board.lists[listIndex + 1].title} →
                      </button>
                    )}
                  </div>
                </article>
              ))}
            </div>

            <form onSubmit={(e) => handleAddTask(e, list.id)} className="add-task-form">
              <input
                type="text"
                placeholder="New task title..."
                value={taskTitles[list.id] || ''}
                onChange={(e) =>
                  setTaskTitles((prev) => ({ ...prev, [list.id]: e.target.value }))
                }
              />
              <input
                type="text"
                placeholder="Description (optional)"
                value={taskDescriptions[list.id] || ''}
                onChange={(e) =>
                  setTaskDescriptions((prev) => ({ ...prev, [list.id]: e.target.value }))
                }
              />
              <button type="submit">+ Add Card</button>
            </form>
          </section>
        ))}
      </main>
    </div>
  );
}

export default App;
