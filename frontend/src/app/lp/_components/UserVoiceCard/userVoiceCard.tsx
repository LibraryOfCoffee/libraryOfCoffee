import './userVoiceCard.css';

interface QA {
  question: string;
  answers: string[];
}

interface UserVoiceCardProps {
  imageUrl: string;
  userName: string;
  userAge: string;
  userGender: string;
  userOccupation: string;
  tags: string[];
  comment: string;
}

export default function UserVoiceCard({
  imageUrl,
  userName,
  userAge,
  userGender,
  userOccupation,
  tags,
  comment,
}: UserVoiceCardProps) {
  return (
    <div className="user-voice-card">
      <div className="user-voice-card-image">
        <img src={imageUrl} alt={userName} />
      </div>
      <div className="user-voice-card-content">
        <h3 className="user-voice-card-title">
          {userAge} {userGender} {userOccupation} {userName}
        </h3>
        <div className="user-voice-card-tags">
          {tags.map((tag, index) => (
            <div key={index} className="user-voice-card-tag">
              #{tag}
            </div>
          ))}
        </div>
        <div className="user-voice-card-qa-list">
          {comment}
        </div>
      </div>
    </div>
  );
}

