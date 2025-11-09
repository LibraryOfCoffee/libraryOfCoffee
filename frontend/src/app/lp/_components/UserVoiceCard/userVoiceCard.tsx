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
  tags: string[];
  qaList: QA[];
}

export default function UserVoiceCard({
  imageUrl,
  userName,
  userAge,
  userGender,
  tags,
  qaList,
}: UserVoiceCardProps) {
  return (
    <div className="user-voice-card">
      <div className="user-voice-card-image">
        <img src={imageUrl} alt={userName} />
      </div>
      <div className="user-voice-card-content">
        <h3 className="user-voice-card-title">
          {userName}（{userAge} {userGender}）
        </h3>
        <div className="user-voice-card-tags">
          {tags.map((tag, index) => (
            <div key={index} className="user-voice-card-tag">
              #{tag}
            </div>
          ))}
        </div>
        <div className="user-voice-card-qa-list">
          {qaList.map((qa, index) => (
            <div key={index} className="user-voice-card-qa-item">
              <h4 className="user-voice-card-question">― {qa.question}</h4>
              {
                qa.answers.map((answer, index) => {
                  return <p key={index} className="user-voice-card-answer">{answer}</p>
                })
              }
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

