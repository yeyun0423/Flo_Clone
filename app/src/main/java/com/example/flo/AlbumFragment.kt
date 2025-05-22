package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var db: AppDatabase
    private lateinit var likeDao: LikeDao
    private var userId: Int = -1
    private var albumId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        db = AppDatabase.getInstance(requireContext())
        likeDao = db.likeDao()

        val sharedPreferences = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPreferences.getInt("jwt", -1)

        // 전달받은 앨범 정보
        albumId = arguments?.getInt("albumId") ?: -1
        val title = arguments?.getString("title")
        val singer = arguments?.getString("singer")
        val coverImg = arguments?.getInt("coverImg")

        Log.d("AlbumFragment", "userId=$userId, albumId=$albumId")

        // UI에 앨범 정보 표시
        binding.albumMusicTitleTv.text = title ?: "제목 없음"
        binding.albumSingerNameTv.text = singer ?: "가수 없음"
        coverImg?.let {
            binding.albumAlbumIv.setImageResource(it)
        }

        // 초기 하트 상태 설정 및 클릭 이벤트
        if (userId != -1 && albumId != -1) {
            val isLiked = likeDao.isLiked(userId, albumId)
            setHeartIcon(isLiked)

            binding.albumLikeIv.setOnClickListener {
                val currentLiked = likeDao.isLiked(userId, albumId)
                Log.d("AlbumFragment", "하트 클릭됨, 현재 liked = $currentLiked")
                if (currentLiked) {
                    likeDao.deleteLike(userId, albumId)
                    setHeartIcon(false)
                } else {
                    likeDao.insertLike(Like(userId, albumId))
                    setHeartIcon(true)
                }
            }
        }

        // 뒤로가기 버튼
        binding.albumBackIv.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    // 좋아요 여부에 따라 하트 아이콘 설정
    private fun setHeartIcon(liked: Boolean) {
        Log.d("AlbumFragment", "하트 상태: liked = $liked")
        binding.albumLikeIv.setImageResource(
            if (liked) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
        )
    }
}
