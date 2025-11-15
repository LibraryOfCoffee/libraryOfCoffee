import './userVoiceCard.css';
import Image from "next/image";

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
        <Image
          src={imageUrl}
          alt={userName}
          height={200}
          width={1000}
        />
      </div>
      <div className="user-voice-card-content">
        <h3 className="user-voice-card-title">
          {userAge} {userGender} {userOccupation} {userName}
        </h3>
        <div className="user-voice-card-tags">
          {tags.map((tag, index) => (
            <span key={index} className="user-voice-card-tag">
              #{tag}
            </span>
          ))}
        </div>
        <p className="user-voice-card-qa-list">
          {comment}
        </p>
      </div>
    </div>
  );
}

