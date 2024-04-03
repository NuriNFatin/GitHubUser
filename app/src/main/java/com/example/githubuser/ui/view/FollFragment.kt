import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.adapter.ListGitAdapter
import com.example.githubuser.databinding.FragmentFollBinding
import com.example.githubuser.ui.viewModel.FollowersViewModel
import com.example.githubuser.ui.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _position: Int? = null
    private var _username: String? = null
    private var _binding: FragmentFollBinding? = null
    private val binding get() = _binding
    private val followersViewModel by viewModels<FollowersViewModel>()
    private val followingViewModel by viewModels<FollowingViewModel>()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar=binding?.progressBar!!

        arguments?.let {
            _position = it.getInt(ARG_POSITION)
            _username = it.getString(ARG_USERNAME)
        }
        val adapter = ListGitAdapter()

        setupRecyclerView()

        if (_position == 1) {
            showFollowersData(adapter)
        } else {
            showFollowingData(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)
    }

    private fun showFollowingData(adapter: ListGitAdapter) {
        progressBar.visibility = View.VISIBLE

        followingViewModel.getUserFollowingByUsername(_username!!)

        followingViewModel.listfollowing.observe(viewLifecycleOwner) { followingList ->
            progressBar.visibility = View.GONE
            adapter.submitList(followingList)
            binding?.rvFollow?.adapter = adapter
        }

        followingViewModel.noFollowingAnyoneText.observe(viewLifecycleOwner) { text ->
            binding?.tvFollowNotFound?.text = text
        }
    }

    private fun showFollowersData(adapter: ListGitAdapter) {
        progressBar.visibility = View.VISIBLE

        followersViewModel.getUserFollowersByUsername(_username!!)

        followersViewModel.listfollowers.observe(viewLifecycleOwner) { followersList ->
            progressBar.visibility = View.GONE
            adapter.submitList(followersList)
            binding?.rvFollow?.adapter = adapter
        }

        followersViewModel.noFollowersAnyoneText.observe(viewLifecycleOwner) { text ->
            binding?.tvFollowNotFound?.text = text
        }
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}